package de.doubleslash.keeptime.viewpopup;

import java.awt.Point;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;

/**
 * Class captures all key events and implements a hotkey mechanism. Needs to be stopped by calling shutdown.
 * 
 * @author nmutter
 */
public class GlobalScreenListener implements NativeKeyListener, NativeMouseMotionListener {
   private static final int LEFT_WINDOWS_CODE = 91;

   private static final int LEFT_CTRL_CODE = 162;

   private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this.getClass());

   private ViewControllerPopup viewController;

   private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

   private ScheduledFuture<?> resetKeyPressesFuture;

   private final Runnable resetPressedKeysRunnable = () -> {
      LOG.debug("Resetting pressed keys");
      controllPressed = false;
      windowsPressed = false;
   };

   // TODO find better hotkey - maybe configurable?
   private boolean controllPressed = false;
   private boolean windowsPressed = false;

   private Point mouseLocation = new Point(0, 0);

   public GlobalScreenListener() {

      disableJNativeHookLogger();

      GlobalScreen.addNativeKeyListener(this);
      GlobalScreen.addNativeMouseMotionListener(this);
   }

   public void register(final boolean register) {
      try {
         if (register) {
            LOG.info("Registering native hook");
            GlobalScreen.registerNativeHook();
         } else {
            LOG.info("Unregistering native hook");
            GlobalScreen.unregisterNativeHook();
         }
      } catch (final NativeHookException ex) {
         LOG.error("Error whill (un)registering natvice hooks.", ex);
      }
   }

   private void disableJNativeHookLogger() {
      // Get the logger for "org.jnativehook" and set the level to off.
      final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());

      logger.setLevel(Level.OFF);

      // Don't forget to disable the parent handlers
      logger.setUseParentHandlers(false);
   }

   public void setViewController(final ViewControllerPopup viewController) {
      this.viewController = viewController;
   }

   @Override
   public void nativeKeyPressed(final NativeKeyEvent e) {
      switch (e.getRawCode()) {
      case LEFT_CTRL_CODE:
         controllPressed = true;
         break;
      case LEFT_WINDOWS_CODE:
         windowsPressed = true;
         break;
      default:
         return;
      }

      if (controllPressed && windowsPressed) {
         Platform.runLater(() -> {
            viewController.show(mouseLocation);
         });
      }

      // Logic to reset key presses if no input for n seconds
      // if u win+l (lock) windows, the key release events are not received
      if (resetKeyPressesFuture != null) {
         resetKeyPressesFuture.cancel(false);
      }
      resetKeyPressesFuture = executor.schedule(resetPressedKeysRunnable, 1, TimeUnit.SECONDS);
   }

   @Override
   public void nativeKeyReleased(final NativeKeyEvent e) {
      switch (e.getRawCode()) {
      case LEFT_CTRL_CODE:
         controllPressed = false;
         break;
      case LEFT_WINDOWS_CODE:
         windowsPressed = false;
         break;
      default:
         return;
      }

      if (!controllPressed && !windowsPressed && resetKeyPressesFuture != null) {
         // nothing to reset so we can cancel
         resetKeyPressesFuture.cancel(false);
      }
   }

   @Override
   public void nativeKeyTyped(final NativeKeyEvent e) {
      // Not needed
   }

   @Override
   public void nativeMouseMoved(final NativeMouseEvent nativeEvent) {
      mouseLocation = nativeEvent.getPoint();
   }

   @Override
   public void nativeMouseDragged(final NativeMouseEvent nativeEvent) {
      // Not needed
   }

   /**
    * Shuts down the threads and listeners. Instance is not usable anymore after calling this.
    */
   public void shutdown() {
      executor.shutdown();
      register(false);
   }
}