package chatdist.backend.repository;

import chatdist.backend.model.Statistics;
import org.springframework.data.repository.CrudRepository;

public interface StatisticsRepository  extends CrudRepository<Statistics, Long> {
}