package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaTutorial1 {

    public static void main(String[] args) {
        /* Lodding 시점에 딱 하나만 만들기 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        /* 일관적인 단위를 할때 마다 Entity Manager를 생성해야 한다.*/
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /* 정석 코드 */
        try {
            Member findMember = em.find(Member.class, 1L);

            /* 회원 찾기*/
//            System.out.println("findMember.id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());
            /* 회원 수정 */
//            findMember.setName("HelloJPA");



            /* 전체 회워 조회 */
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member.name = " + member.getName());
            }


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
