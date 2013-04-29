package ar.edu.untdf.model;

import ar.edu.untdf.model.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-04-27T10:25:54")
@StaticMetamodel(Money.class)
public class Money_ { 

    public static volatile SingularAttribute<Money, Long> id;
    public static volatile SingularAttribute<Money, String> siglas;
    public static volatile SingularAttribute<Money, String> descripcion;
    public static volatile ListAttribute<Money, Usuario> usuarios;

}