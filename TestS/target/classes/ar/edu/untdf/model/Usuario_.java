package ar.edu.untdf.model;

import ar.edu.untdf.model.Moneda;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-28T20:52:30")
@StaticMetamodel(Usuario.class)
public class Usuario_ { 

    public static volatile SingularAttribute<Usuario, Long> id;
    public static volatile SingularAttribute<Usuario, String> passwd;
    public static volatile SingularAttribute<Usuario, String> name;
    public static volatile ListAttribute<Usuario, Moneda> moneyList;

}