package dev.aknb.ordersystem.repositories.user;

import dev.aknb.ordersystem.dtos.user.UserFilterDto;
import dev.aknb.ordersystem.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<User> findUserByFilter(UserFilterDto userFilterDto) {

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cQuery = cBuilder.createQuery(User.class);
        Root<User> userRoot = cQuery.from(User.class);

        cQuery.where(
                getPredicates(userFilterDto, cBuilder, userRoot)
        );

        cQuery.orderBy(cBuilder.desc(userRoot.get("lastModifiedDate")));

        TypedQuery<User> userQuery = entityManager.createQuery(cQuery);
        userQuery.setFirstResult(userFilterDto.getPageable().getPageNumber() * userFilterDto.getPageable().getPageSize());
        userQuery.setMaxResults(userFilterDto.getPageable().getPageSize());

        List<User> users = userQuery.getResultList();

        CriteriaQuery<Long> countCQuery = cBuilder.createQuery(Long.class);
        Root<User> countUserRoot = countCQuery.from(User.class);

        countCQuery.select(cBuilder.count(countUserRoot));

        countCQuery.where(
                getPredicates(userFilterDto, cBuilder, countUserRoot)
        );

        Long count = entityManager.createQuery(countCQuery).getSingleResult();

        return new PageImpl<>(users, userFilterDto.getPageable(), count);
    }

    private static Predicate getPredicates(UserFilterDto userFilterDto, CriteriaBuilder cBuilder, Root<User> userRoot) {

        return cBuilder.and(
                cBuilder.equal(userRoot.get("status"), userFilterDto.getStatus()),
                cBuilder.or(
                        cBuilder.like(cBuilder.lower(userRoot.get("fullName")), "%" + userFilterDto.getSearchText().toLowerCase() + "%"),
                        cBuilder.like(cBuilder.lower(userRoot.get("phoneNumber")), "%" + userFilterDto.getSearchText().toLowerCase() + "%")
                )
        );
    }
}
