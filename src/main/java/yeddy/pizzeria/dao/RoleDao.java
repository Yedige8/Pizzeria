package yeddy.pizzeria.dao;

import yeddy.pizzeria.dao.factory.Dao;
import yeddy.pizzeria.model.dto.Role;

import java.util.Optional;

public interface RoleDao extends Dao {

  Optional<Role> findById(long roleId);

  Optional<Role> findByServiceName(String serviceName);
}
