package ru.morningcake.component;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.morningcake.exception.data.BadDataException;
import ru.morningcake.utils.CrudUtils;

import javax.validation.constraints.Min;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class FifoCache<UserId,ID,T> {

  @Setter
  protected Integer cacheSize = 1000;
  private final Map<UserId, HashMap<ID,T>> cacheMaps = new ConcurrentHashMap<>();
  private final Map<UserId, ArrayDeque<ID>> idIndexMaps = new ConcurrentHashMap<>();

  /** При создании поста обновить кэш при его наличии всем друзьям */
  public void addCacheForAll(Set<UserId> friendsIds, ID id, T entity) {
    for (UserId friendId : friendsIds) {
      if (cacheMaps.containsKey(friendId)) {
        cacheAdd(friendId, id, entity);
      }
    }
  }

  private void cacheAdd(UserId userId, ID id, T entity) {
    if (!cacheMaps.containsKey(userId)) {
      HashMap<ID,T> userCacheQueue = new HashMap<>(cacheSize, 1.1f);
      userCacheQueue.put(id, entity);
      cacheMaps.put(userId, userCacheQueue);
      ArrayDeque<ID> userIdIndexes = new ArrayDeque<>(cacheSize);
      userIdIndexes.addFirst(id);
      idIndexMaps.put(userId, userIdIndexes);
      log.debug("User {}: entity {} - add queue and cache", userId, id);
    }
    HashMap<ID,T> userCacheQueue = cacheMaps.get(userId);
    ArrayDeque<ID> userIdIndexes = idIndexMaps.get(userId);
    if (userCacheQueue.size() == cacheSize) {
      ID removedId = userIdIndexes.pollLast();
      userCacheQueue.remove(removedId); // удалить последний элемент
      log.debug("User {}: entity {} - evict last cache", userId, id);
    }
    userCacheQueue.put(id, entity);
    userIdIndexes.addFirst(id);
    log.debug("User {}: entity {} - add cache", userId, id);
  }

  /** При обновлении поста обновить кэш при его наличии всем друзьям */
  public void putCacheForAll(Set<UserId> friendsIds, ID id, T entity) {
    for (UserId friendId : friendsIds) {
      if (cacheMaps.containsKey(friendId)) {
        cachePut(friendId, id, entity);
      }
    }
  }

  private void cachePut(UserId userId, ID id, T entity) {
    if (cacheMaps.containsKey(userId)) {
      HashMap<ID,T> userQueue = cacheMaps.get(userId);
      if (userQueue.containsKey(id)) {
        userQueue.put(id,entity);
        log.debug("User {}: entity {} - put cache", userId, id);
      }
    }
  }

  /** При удалении поста обновить кэш при его наличии всем друзьям */
  public void evictCacheForAll(Set<UserId> friendsIds, ID id) {
    for (UserId friendId : friendsIds) {
      if (cacheMaps.containsKey(friendId)) {
        cacheEvict(friendId, id);
      }
    }
  }

  private void cacheEvict(UserId userId, ID id) {
    if (cacheMaps.containsKey(userId)) {
      HashMap<ID,T> userQueue = cacheMaps.get(userId);
      userQueue.remove(id);
      ArrayDeque<ID> userIdIndexes = idIndexMaps.get(userId);
      userIdIndexes.remove(id);
      log.debug("User {}: entity {} - evict cache", userId, id);
      if (userQueue.isEmpty() && userIdIndexes.isEmpty()) {
        cacheMaps.remove(userId);
        idIndexMaps.remove(userId);
        log.debug("User {} - remove empty queue", userId);
      }
    }
  }

  /** Получить ленту постов из кэша */
  @SneakyThrows
  public List<T> getPosts(UserId userId, @Min(1) int pageNum, @Min(1) int potentialPageSize) throws BadDataException {
    if (!cacheMaps.containsKey(userId)) return List.of();
    HashMap<ID,T> userCacheQueue = cacheMaps.get(userId);
    ArrayDeque<ID> userIdIndexes = idIndexMaps.get(userId);
    int startIndex = CrudUtils.calcOffset(pageNum, potentialPageSize);
    if (startIndex >= userCacheQueue.size()) throw new BadDataException("Page num is incorrect!");
    int realPageSize = potentialPageSize < userCacheQueue.size() ? potentialPageSize : userCacheQueue.size();
    int stopIndex = startIndex + realPageSize-1;
    List<T> page = new ArrayList<>(realPageSize);
    ID[] idsArray = (ID[]) userIdIndexes.toArray();
    for (int i = startIndex; i <= stopIndex; i++) {
      page.add(userCacheQueue.get(idsArray[i]));
    }
    log.debug("User {} - get cache page, size = {}", userId, realPageSize);
    return page;
  }

  /** Прогрев кэша. Порядок сортировки списков сущностей и ключей д.б.одинаковым */
  @SneakyThrows
  public void warming(UserId userId, List<T> entities, List<ID> ids) {
    if (cacheMaps.containsKey(userId) || idIndexMaps.containsKey(userId)) return;
    if (entities.size() != ids.size()) throw new BadDataException("Entities list size is not equals ids size!");
    HashMap<ID,T> cacheQueue = new HashMap<>(cacheSize, 1.1f);
    ArrayDeque<ID> idsIndexes = new ArrayDeque<>(cacheSize);
    for (int i = entities.size()-1; i >= 0; i--) {
      cacheQueue.put(ids.get(i), entities.get(i));
      idsIndexes.addFirst(ids.get(i));
    }
    cacheMaps.put(userId, cacheQueue);
    idIndexMaps.put(userId, idsIndexes);
  }


}
