package io.crnk.example.service.jpa;

import io.crnk.jpa.JpaEntityRepositoryBase;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MovieRepository extends JpaEntityRepositoryBase<MovieEntity, UUID> {

    public MovieRepository() {
        super(MovieEntity.class);
    }

    @Override
    public MovieEntity save(MovieEntity entity) {
        // add your save logic here
        return super.save(entity);
    }

    @Override
    public MovieEntity create(MovieEntity entity) {
        // add your create logic here
        return super.create(entity);
    }

    @Override
    public void delete(UUID id) {
        // add your save logic here
        super.delete(id);
    }
}
