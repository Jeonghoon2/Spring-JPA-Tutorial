package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/* 캐쉬 테스트 */
public class JpaTutorial2 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // 영속
            Member member1 = new Member();
            member1.setUserName("MemberA");
            Member member2 = new Member();
            member2.setUserName("MemberB");
            Member member3 = new Member();
            member3.setUserName("MemberC");

            System.out.println("======================");
            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            System.out.println("memeber.id = "+ member1.getId());
            System.out.println("memeber.id = "+ member2.getId());
            System.out.println("memeber.id = "+ member3.getId());
            System.out.println("======================");

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
    }
}
