package com.osman.questionservice.repo;

import com.osman.questionservice.model.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Question> search(String category, String keyword) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Question> cq = cb.createQuery(Question.class);
        Root<Question> root = cq.from(Question.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(cb.equal(root.get("category"), category));
        }
        if (keyword != null) {
            predicates.add(cb.like(root.get("questionTitle"), "%" + keyword + "%"));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getResultList();
    }
}