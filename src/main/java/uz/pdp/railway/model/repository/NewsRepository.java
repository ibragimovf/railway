package uz.pdp.railway.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.railway.model.entity.NewsEntity;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
}
