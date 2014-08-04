Task 8
------

Spring offers caching for slow service or repository operations. Enable caching for the database respositories
if destinations or connections with up to one stop are requested or up to 20 airports.

Tips:

* you may must not cache the results of the _airports_ command without country argument
* use `@Cacheable` to mark the methods, have a look at the `condition` attribute
* use `@EnableCaching` to enable caching, configure a cache manager as required
* configure logging to debug for test the cache
* first use the `ConcurrentMapCacheManager` (without restriction to 20 airports), later use
  [Ehcache](http://ehcache.org/)