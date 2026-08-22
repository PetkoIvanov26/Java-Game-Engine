package org.twingolfpapa.engine.time;

/** A monotonic clock suitable for measuring elapsed time. */
@FunctionalInterface
public interface MonotonicClock {

    long nowNanos();

    static MonotonicClock system() {
        return System::nanoTime;
    }
}
