package project.com.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transaction_item", schema = "public")
public class TransactionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", foreignKey = @ForeignKey(name = "lesson"))
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "transaction_id", foreignKey = @ForeignKey(name = "transaction"))
    private TransactionHistory transaction;
}

