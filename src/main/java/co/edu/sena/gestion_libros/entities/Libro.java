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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "libro", catalog = "db_libros", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l")
    , @NamedQuery(name = "Libro.findByLibId", query = "SELECT l FROM Libro l WHERE l.libId = :libId")
    , @NamedQuery(name = "Libro.findByLibNombre", query = "SELECT l FROM Libro l WHERE l.libNombre = :libNombre")
    , @NamedQuery(name = "Libro.findByLibNumeroPaginas", query = "SELECT l FROM Libro l WHERE l.libNumeroPaginas = :libNumeroPaginas")
    , @NamedQuery(name = "Libro.findByLibFechaPublicacion", query = "SELECT l FROM Libro l WHERE l.libFechaPublicacion = :libFechaPublicacion")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "libId", nullable = false)
    private Integer libId;
    @Basic(optional = false)
    @Column(name = "libNombre", nullable = false, length = 30)
    private String libNombre;
    @Basic(optional = false)
    @Column(name = "libNumeroPaginas", nullable = false)
    private short libNumeroPaginas;
    @Basic(optional = false)
    @Column(name = "libFechaPublicacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date libFechaPublicacion;
    @JoinColumn(name = "catId", referencedColumnName = "catId", nullable = false)
    @ManyToOne(optional = false)
    private Categoria catId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "libId")
    private List<AutorLibro> autorLibroList;

    public Libro() {
    }

    public Libro(Integer libId) {
        this.libId = libId;
    }

    public Libro(Integer libId, String libNombre, short libNumeroPaginas, Date libFechaPublicacion) {
        this.libId = libId;
        this.libNombre = libNombre;
        this.libNumeroPaginas = libNumeroPaginas;
        this.libFechaPublicacion = libFechaPublicacion;
    }

    public Integer getLibId() {
        return libId;
    }

    public void setLibId(Integer libId) {
        this.libId = libId;
    }

    public String getLibNombre() {
        return libNombre;
    }

    public void setLibNombre(String libNombre) {
        this.libNombre = libNombre;
    }

    public short getLibNumeroPaginas() {
        return libNumeroPaginas;
    }

    public void setLibNumeroPaginas(short libNumeroPaginas) {
        this.libNumeroPaginas = libNumeroPaginas;
    }

    public Date getLibFechaPublicacion() {
        return libFechaPublicacion;
    }

    public void setLibFechaPublicacion(Date libFechaPublicacion) {
        this.libFechaPublicacion = libFechaPublicacion;
    }

    public Categoria getCatId() {
        return catId;
    }

    public void setCatId(Categoria catId) {
        this.catId = catId;
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
        hash += (libId != null ? libId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.libId == null && other.libId != null) || (this.libId != null && !this.libId.equals(other.libId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.sena.gestion_libros.entities.Libro[ libId=" + libId + " ]";
    }
    
}
