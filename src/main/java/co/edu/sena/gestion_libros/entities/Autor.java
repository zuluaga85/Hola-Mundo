/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.sena.gestion_libros.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "autor", catalog = "db_libros", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Autor.findAll", query = "SELECT a FROM Autor a")
    , @NamedQuery(name = "Autor.findByAutId", query = "SELECT a FROM Autor a WHERE a.autId = :autId")
    , @NamedQuery(name = "Autor.findByAutNombre", query = "SELECT a FROM Autor a WHERE a.autNombre = :autNombre")
    , @NamedQuery(name = "Autor.findByAutGenero", query = "SELECT a FROM Autor a WHERE a.autGenero = :autGenero")
    , @NamedQuery(name = "Autor.findByAutFechaNacimiento", query = "SELECT a FROM Autor a WHERE a.autFechaNacimiento = :autFechaNacimiento")})
public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "autId", nullable = false)
    private Integer autId;
    @Basic(optional = false)
    @Column(name = "autNombre", nullable = false, length = 30)
    private String autNombre;
    @Basic(optional = false)
    @Column(name = "autGenero", nullable = false, length = 2)
    private String autGenero;
    @Basic(optional = false)
    @Column(name = "autFechaNacimiento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date autFechaNacimiento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autId")
    private List<AutorLibro> autorLibroList;

    public Autor() {
    }

    public Autor(Integer autId) {
        this.autId = autId;
    }

    public Autor(Integer autId, String autNombre, String autGenero, Date autFechaNacimiento) {
        this.autId = autId;
        this.autNombre = autNombre;
        this.autGenero = autGenero;
        this.autFechaNacimiento = autFechaNacimiento;
    }

    public Integer getAutId() {
        return autId;
    }

    public void setAutId(Integer autId) {
        this.autId = autId;
    }

    public String getAutNombre() {
        return autNombre;
    }

    public void setAutNombre(String autNombre) {
        this.autNombre = autNombre;
    }

    public String getAutGenero() {
        return autGenero;
    }

    public void setAutGenero(String autGenero) {
        this.autGenero = autGenero;
    }

    public Date getAutFechaNacimiento() {
        return autFechaNacimiento;
    }

    public void setAutFechaNacimiento(Date autFechaNacimiento) {
        this.autFechaNacimiento = autFechaNacimiento;
    }

    @XmlTransient
    public List<AutorLibro> getAutorLibroList() {
        return autorLibroList;
    }

    public void setAutorLibroList(List<AutorLibro> autorLibroList) {
        this.autorLibroList = autorLibroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (autId != null ? autId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Autor)) {
            return false;
        }
        Autor other = (Autor) object;
        if ((this.autId == null && other.autId != null) || (this.autId != null && !this.autId.equals(other.autId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.sena.gestion_libros.entities.Autor[ autId=" + autId + " ]";
    }
    
}
