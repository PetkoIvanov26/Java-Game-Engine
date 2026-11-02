package org.twingolfpapa.engine.time;

/** Provides monotonic time for elapsed-time measurements. */
@FunctionalInterface
public interface MonotonicClock {

    long nowNanos();

    static MonotonicClock system() {
        return System::nanoTime;
    }
}
