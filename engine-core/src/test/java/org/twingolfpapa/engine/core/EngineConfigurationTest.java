package org.twingolfpapa.engine.core;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EngineConfigurationTest {

    @Test
    void createsAValidatedSixtyHertzConfiguration() {
        EngineConfiguration configuration = EngineConfiguration.atFixedRate(60);

        assertEquals(16_666_667L, configuration.fixedStepNanos());
        assertEquals(0.016666667, configuration.fixedDeltaSeconds(), 0.000000001);
        assertEquals(Duration.ofMillis(250).toNanos(), configuration.maxFrameNanos());
        assertEquals(5, configuration.maxUpdatesPerFrame());
    }

    @Test
    void rejectsInvalidTimingPolicies() {
        assertThrows(IllegalArgumentException.class, () -> new EngineConfiguration(0, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new EngineConfiguration(1, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new EngineConfiguration(1, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> EngineConfiguration.atFixedRate(0));
    }
}
