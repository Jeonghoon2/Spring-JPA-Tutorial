package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        /* Lodding 시점에 딱 하나만 만들기 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        /* 일관적인 단위를 할때 마다 Entity Manager를 생성해야 한다.*/
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /* 정석 코드 */
        try {
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");
            em.persist(member);

            /* 정상적이면 Commit*/
            tx.commit();
        }catch (Exception e){
            /* 문제가 생기면 RollBack */
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }
}
