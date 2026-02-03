package fr.esgi.reseking.repository;

import fr.esgi.reseking.model.TestResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestResponseRepository extends JpaRepository<TestResponse, Integer> {

}
