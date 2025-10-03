package project.com.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "lesson_id" }))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lesson_id", nullable = false)
	private Lesson lesson;
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (createdAt == null)
			createdAt = LocalDateTime.now();
	}
}
