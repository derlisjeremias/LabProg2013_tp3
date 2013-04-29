package ar.edu.untdf.testservices;

import ar.edu.untdf.model.Moneda;
import ar.edu.untdf.model.Usuario;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author matiasgel
 */
public class TestJpa {

    public static EntityManagerFactory factory = Persistence.createEntityManagerFactory("money");
    public static EntityManager em = TestJpa.factory.createEntityManager();        
    
    
    /**
     * Busca una moneda segun sus siglas
     * @param siglas siglas de la moneda a buscar
     * @return  moneda buscada
     */
    public static Moneda getDBMoney(String siglas){
        TypedQuery<Moneda> q= em.createQuery("select m from Moneda m where m.siglas=:siglas", Moneda.class);
        q.setParameter("siglas",siglas);
        return q.getSingleResult();
    }
    
    
    /**
     * Busca un usuario segun su nombre
     * @param name nombre del usuario a buscar
     * @return  usuario
     */
    public static Usuario getDBUser(String name){
        TypedQuery<Usuario> q= em.createQuery("select u from Usuario u where u.name=:name", Usuario.class);
        q.setParameter("name", name);
        return q.getSingleResult();
    }
    
    
    /**
     * carga el listado de monedas de open exchange rates en la base de datos
     */    
    public static void loadMoney() {
        try {
            String request = "http://openexchangerates.org/api/currencies.json";
            HttpClient client = new DefaultHttpClient();
            HttpGet method = new HttpGet(request);
            HttpResponse response = client.execute(method);
            HttpEntity entity = response.getEntity();
            InputStream rstream = entity.getContent();
            StringWriter writer = new StringWriter();
            IOUtils.copy(rstream, writer);
            String jsonString = writer.toString();
            JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonString);            
            em.getTransaction().begin();
            for (Iterator<String> i = json.keys(); i.hasNext();) {
                String key = i.next();
                Moneda m = new Moneda();
                m.setSiglas(key);
                m.setDescripcion(json.getString(key));
                em.persist(m);
            }
            em.getTransaction().commit();


        } catch (IOException ex) {
            Logger.getLogger(TestJpa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Carga usuarios de prueba en la base de datos
     */
    public static void loadUsers(){                
        TypedQuery<Moneda> q= em.createQuery("select m from Moneda m where m.siglas=:siglas", Moneda.class);
        
        Moneda aed= getDBMoney("AED");        
        Moneda cop=getDBMoney("COP");        ;
        Moneda crc=getDBMoney("CRC");                
        Moneda ars=getDBMoney("ARS");        
        em.getTransaction().begin();
        Usuario u = new Usuario();
        u.setName("matiasgel");
        u.setPasswd("1234");        
        u.getMoneyList().add(ars);
        u.getMoneyList().add(crc);
        em.persist(u);
        u= new Usuario();
        u.setName("pablo");
        u.setPasswd("123");
        u.getMoneyList().add(aed);
        u.getMoneyList().add(crc);
        u.getMoneyList().add(cop);
        em.persist(u);
        em.getTransaction().commit();
    }
    
        
    
    /**
     * Imprime todos los usuarios y sus monedas
     */
    public static void printUsers(){
        Query q = em.createQuery("select u from Usuario u");
        List<Usuario> users=q.getResultList();
        for(Usuario u:users){
            System.out.println("-----------------");
            System.out.println(u);
            for(Moneda m:u.getMoneyList())
                System.out.println(m);
            System.out.println("*****************");
        }
    }
    
    
    /**
     * Muestra el listado total de monedas de la base de datos en la salida
     * estandar
     */
    public static void printDatabaseMoney(){
    EntityManager em = TestJpa.factory.createEntityManager();
    Query q=em.createQuery("select m from Money m");
    List<Moneda> monedas = q.getResultList();
    for(Moneda moneda:monedas){
        System.out.println(moneda);
    }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        loadMoney();
        loadUsers();
        printUsers();
        em.getTransaction().begin();
        Usuario u=getDBUser("matiasgel");
        u.getMoneyList().add(getDBMoney("BBD"));        
        em.getTransaction().commit();
        printUsers();
        em.close();
    }
}
