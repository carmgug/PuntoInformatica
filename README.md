# PuntoInformatica
Progetto Piattaforme Carmelo Gugliotta mat:213477
## Appunti

### why filed injection is not recommended
https://cloudolife.com/2021/02/27/Programming-Language/Java/FAQs/Field-injection-is-not-recommended-and-Injection-guidelines-in-Java-Spring/#:~:text=The%20reasons%20why%20field%20injection,in%20unit%20tests)%20without%20reflection.
### cosa effettua la merge
merge: 
    Find an attached object with the same id and update it. 
    If exists update and return the already attached object. 
    If doesn't exist insert the new register to the database.
### Gestire le impaginazioni
Per gestire le impaginazioni:
    PageImpl(List<T> content, Pageable pageable, long total)
where:
    content – the content of this page(Your collection object).
    pageable – the paging information
    total – the total amount of items available.
### Descrizione lock
https://www.baeldung.com/jpa-optimistic-locking Per capire le lock.

### PersistenceContext vs Autowired for EntityManger
The persistence context contains entity instances and used to manage the entity instances life cycle.
https://img-blog.csdnimg.cn/20200414184500504.png
First @PersistenceContext is a special annotation of jpa, and @Autowired is the annotation that comes with spring. The
picture above means that EntityManager is not thread-safe. When multiple requests come in, spring will create multiple 
threads, and @PersistenceContext It is used to create an EntityManager for each thread, and @Autowired only creates one, 
which is shared by all threads and may report errors

### Come generare un certificato ssl auto-signed
https://www.thomasvitale.com/https-spring-boot-ssl-certificate/

### extends WebConfigurerAdapter DEPRECATED
https://www.codejava.net/frameworks/spring-boot/fix-websecurityconfigureradapter-deprecated
### keycloack back end
https://medium.com/@kamleshbadgujar00/secure-spring-boot-angular-9-application-using-keycloak-1-3-b00e801ba693