package io.crnk.example.service.basic;

import io.crnk.core.repository.InMemoryResourceRepository;
import org.springframework.stereotype.Component;

/**
 * Showcases a simple in-memory repository.
 */
@Component
public class LocationRepository extends InMemoryResourceRepository<Location, String> {

    public LocationRepository() {
        super(Location.class);
    }
}