package de.doubleslash.keeptime;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler logging uncaught exceptions. Gotta Catch 'Em All
 * 
 * @author nmutter
 */
public class DefaultExceptionHandler implements UncaughtExceptionHandler {

   private final Logger log = LoggerFactory.getLogger(this.getClass());

   @Override
   public void uncaughtException(final Thread t, final Throwable e) {
      log.error("Uncaught exception on thread '{}'.", t, e);
   }

   /**
    * Registers this class as default uncaught exception handler
    */
   public void register() {
      log.debug("Registering uncaught exception handler");
      final UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
      if (defaultUncaughtExceptionHandler != null) {
         log.warn("Uncaught exception handler was already set ('{}'). Overwritting.", defaultUncaughtExceptionHandler);
      }
      Thread.setDefaultUncaughtExceptionHandler(this);
   }

}
