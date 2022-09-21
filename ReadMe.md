# JPA

### JPA에서 가장 중요한 2가지
* **객체와 관계형 데이터베이스 매핑하기 ( Object Relational Mapping )**
* **영속성 컨텍스트**
  * 엔티티를 영구 저장하는 환경
  * EntityManager.persist(entity);
  * Entity Manager를 통해서 영속성 컨텍스트에 접근

### 용어 정리
* **비영속 (new/transient)** 
  * 영속석 컨텍스트와 전혀 관계가 없는 **새로운** 상태
  * 객체를 생성하고 entityManager에 속해있지 않는 것
```java
    User user = new User();
    user.setId("User Id");
    user.setName("User Name");
```
* **영속 (managed)**
  * 영속성 컨텍스트에 **관리**되는 상태
  * 객체를 생성하고 EntityManager에 속해 있는 것
  * 영속 상태가 되어도 바로 DB에 변화를 주지 않는다.
```java
    User user = new User();
    user.setId("User Id");
    user.setName("User Name");
    
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    
    /* 객체를 저장한 상태 (영속) */
    em.persist(user);
```
* **준영속(detached)**
  * 영속성 컨텍스에 저장되었다가 **분리**된 상태
```java
    /* 위와 동일 코드 */

    em.detach(user);
```
* **삭제 (removed)**
  * **삭제**된 상태
```java
    /* 위와 동일 코드 */
    
    em.rmove(user);
```

### 영속성 컨텍스트의 이점
* **1차 캐시**
  * 예시로 Primary Key인 '**User Id**'를 검색할때 DB와 네트워크 통신을 바로 하지 않고
  1차 캐시에 저장 되어 있는지 검사하고 1차 캐시에 있다면 1차 캐시에 저장되어 있는 값을 반환
* **영속 엔티티의 동일성(identity) 보장**
  * 1차 캐시로 반복 가능한 읽기 등급의 트랜잭션 격리 수준을 데이터베이스가 아닌 애플리케이션 차원에서 제공
```java
    User user1 = em.find(User.class, "user");
    User user2 = em.find(User.class, "user");
    System.out.println(user1 == user2);
```

```shell
    true
```
* **트랜잭션을 지원하는 쓰기 지연**
* **변경 감지**
  * 1차 캐시에서 Entity와 스냅샷을 비교해서 변화가 있다면 자동으로 변경을 감지함
  * 이 동작은 Commit 시점에 일어남
* **지연 로딩**

### 플러시
>영속성 컨텍스트의 변경내용을 데이터베이스에 반영
* 변경감지
* 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
* 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송 (등록, 수정, 삭제 쿼리)
* 플러시가 실행되어도 영속성 컨텍스트는 비워지지 않음
* 플러시는 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화 하는것
* 트랜잭션이라는 작업 단위가 중요함
  * 커밋 직전에만 동기화 하면 됨

#### 영속성 컨텍스트를 플러시하는 방법
* em.flush() - 직접 호출
```java
    User user = new Member(1L,"User1");
    em.persist(user);
    
    // 직접 호출
    em.flush;
    
    tx.commit();
```
* 트랜잭션 커밋 - 플러시 자동 호출
* JPQL 쿼리 실행 - 플러시 자동 호출

#### 플러시 모드 옵션
>em.setFlushMode(FlushModeType.COMMIT)
* FlushModeType.AUTO : 커밋이나 쿼리를 실행할 때 플러시 (기본값)
* FlushModeType.COMMIT : 커밋할 때만 플러시

### 준영속 상태
* 영속 상태의 엔티티가 영속성 컨텍스트에서 분리
* 영속성 컨텍스트가 제공하는 기능을 사용 못함

### 준영속 상태로 만드는 방법
* em.detach(entity) : 특정 엔티티만 준영속 상태로 전환
```java
    User user = new Member(1L,"User1");
    em.persist(user);
    
    // 영속성 컨텍스트에서 분리
    em.detach(user);

    tx.commit();
```

* em.clear() : 영속성 컨텍스트를 완전히 초기화
```java
    User user = new Member(1L,"User1");
    em.persist(user);
    
    // 영속성 컨텍스트를 초기화
    em.close();

    tx.commit();
```
* em.close() : 영속성 컨텍스트를 종료

### 엔티티 매핑
* 객체와 테이블 매핑 : **@Entity, @Table**
* 필드와 컬럼 매핑 : **@Column**
* 기본 키 매핑 : **@Id**
* 연관관계 매핑 : **@ManyToOne, @JoinColumn**

>Entity
* @Entity가 붙은 클래스는 JPA가 관리
* JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수
* 주의
  * 기본 생성자 필수
  * final 클래스, enum, interface, inner 클래스 사용 ❌
  * 저장할 필드에 final 사용 ❌

>Table
* @Table은 엔티티와 매핑할 DB 테이블 지정

### 데이터 베이스 스키마 자동 생성
* DDL을 애플리케이션 실행 시점에 자동 생성
* 테이블 중신 -> 객체 중심
* 데이터베이스 방언을 활용해서 데이터베이스에 맞는 적절한 DDL 생성
* 자동으로 생성된 DDL은 개발 장비에서만 사용하고 운영서버에 사용할시에는 적절히 다듬은 후에 사용
> hibernate.hbm2ddl.auto
* create : 기존테이블 삭제 후 다시 생성 (DROP + CREATE)
* create-drop : create와 같으나 종료시점에 테이블 DROP
* update : 변경분만 반영(운영 DB에는 사용하면 안됨)
* validate : 엔티티와 테이블이 정상 매핑되었는지만 확인
* none : 사용하지 않음
> 데이터베이스 스키마 자동 생성 - 주의
* 운영 장비에는 절대 create, create-drop, update 사용하면 안된다.
* 개발 초기 단계는 create 또는 Update
* 테스트 서버는 update 또는 validate
* 스테이징과 운영 서버는 validate 또는 none


