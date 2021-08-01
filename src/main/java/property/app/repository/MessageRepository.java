package property.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import property.app.beans.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByReceiverId(Long id);

    List<Message> findBySenderId(Long id);
}
