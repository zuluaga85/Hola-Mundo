/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.sena.gestion_libros.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "autor_libro", catalog = "db_libros", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AutorLibro.findAll", query = "SELECT a FROM AutorLibro a")
    , @NamedQuery(name = "AutorLibro.findByAlId", query = "SELECT a FROM AutorLibro a WHERE a.alId = :alId")})
public class AutorLibro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "alId", nullable = false)
    private Integer alId;
    @JoinColumn(name = "autId", referencedColumnName = "autId", nullable = false)
    @ManyToOne(optional = false)
    private Autor autId;
    @JoinColumn(name = "libId", referencedColumnName = "libId", nullable = false)
    @ManyToOne(optional = false)
    private Libro libId;

    public AutorLibro() {
    }

    public AutorLibro(Integer alId) {
        this.alId = alId;
    }

    public Integer getAlId() {
        return alId;
    }

    public void setAlId(Integer alId) {
        this.alId = alId;
    }

    public Autor getAutId() {
        return autId;
    }

    public void setAutId(Autor autId) {
        this.autId = autId;
    }

    public Libro getLibId() {
        return libId;
    }

    public void setLibId(Libro libId) {
        this.libId = libId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alId != null ? alId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AutorLibro)) {
            return false;
        }
        AutorLibro other = (AutorLibro) object;
        if ((this.alId == null && other.alId != null) || (this.alId != null && !this.alId.equals(other.alId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.edu.sena.gestion_libros.entities.AutorLibro[ alId=" + alId + " ]";
    }
    
}
