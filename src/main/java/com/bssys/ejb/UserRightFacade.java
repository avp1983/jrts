package com.bssys.ejb;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
@Named
public class UserRightFacade implements Serializable {
  private static final long serialVersionUID = 1L;

  @PersistenceContext
  private transient EntityManager em;

  @SuppressWarnings("unchecked")
  public Set<Integer> getUserRightItems(int userKey) {
    Query query = em.createNativeQuery(
        "select CAST (rights.ACTIONID AS decimal) As ACTIONID\n"
            + "from UMTICUserRights rights \n"
            + "inner join RemoteUserRoles roles on rights.RoleID=roles.RoleID and roles.ICUserID=?1\n"
    )
        .setParameter(1, userKey);

    HashSet<Integer> resSet = new HashSet<>();
    for (BigDecimal oneRight : (List<BigDecimal>) query.getResultList()) {
      resSet.add(oneRight.intValue());
    }

    return resSet;
  }
}
