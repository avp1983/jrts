package com.bssys.jsf;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitializationListener implements ServletContextListener {

  private static final String GLOBAL_CACHE_NAME = "globalCache";
  private static final int TIME_TO_IDLE_SECONDS = 100000;
  private static final int TIME_TO_LIVE_SECONDS = 100000;
  private static final int MAX_ENTRIES_LOCAL_HEAP = 10000;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    CacheManager singletonManager = CacheManager.create();
    singletonManager.addCache(GLOBAL_CACHE_NAME);
    Cache cache = singletonManager.getCache(GLOBAL_CACHE_NAME);
    CacheConfiguration config = cache.getCacheConfiguration();
    config.setTimeToIdleSeconds(TIME_TO_IDLE_SECONDS);
    config.setTimeToLiveSeconds(TIME_TO_LIVE_SECONDS);
    config.setMaxEntriesLocalHeap(MAX_ENTRIES_LOCAL_HEAP);
    config.setMaxEntriesLocalDisk(0);
    config.setOverflowToOffHeap(false);
    sce.getServletContext().setAttribute(GLOBAL_CACHE_NAME, cache);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    sce.getServletContext();
    CacheManager singletonManager = CacheManager.create();
    singletonManager.removeCache(GLOBAL_CACHE_NAME);
    singletonManager.shutdown();
  }
}
