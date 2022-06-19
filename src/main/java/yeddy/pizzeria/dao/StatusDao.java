package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.model.dto.Status;

import java.util.List;
import java.util.Optional;

public interface StatusDao extends Dao {

  Optional<Status> findById(long statusId);

  List<Status> findAll();

  Optional<Status> findByServiceName(String serviceName);
}
