package project.com.model.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.com.model.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);
}
