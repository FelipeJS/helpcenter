package helpcenter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import helpcenter.domain.enumeration.Status;

import helpcenter.domain.enumeration.Empresa;

import helpcenter.domain.enumeration.Problema;

import helpcenter.domain.enumeration.Severidade;

/**
 * A Chamado.
 */
@Entity
@Table(name = "chamado")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Chamado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "empresa", nullable = false)
    private Empresa empresa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "problema", nullable = false)
    private Problema problema;

    @NotNull
    @Size(min = 5)
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "severidade", nullable = false)
    private Severidade severidade;

    @Column(name = "sugestao")
    private String sugestao;

    @NotNull
    @Size(min = 5)
    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @NotNull
    private User solicitante;

    @ManyToOne
    private User executante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public Chamado status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public Chamado empresa(Empresa empresa) {
        this.empresa = empresa;
        return this;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Problema getProblema() {
        return problema;
    }

    public Chamado problema(Problema problema) {
        this.problema = problema;
        return this;
    }

    public void setProblema(Problema problema) {
        this.problema = problema;
    }

    public String getDescricao() {
        return descricao;
    }

    public Chamado descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Severidade getSeveridade() {
        return severidade;
    }

    public Chamado severidade(Severidade severidade) {
        this.severidade = severidade;
        return this;
    }

    public void setSeveridade(Severidade severidade) {
        this.severidade = severidade;
    }

    public String getSugestao() {
        return sugestao;
    }

    public Chamado sugestao(String sugestao) {
        this.sugestao = sugestao;
        return this;
    }

    public void setSugestao(String sugestao) {
        this.sugestao = sugestao;
    }

    public String getEmail() {
        return email;
    }

    public Chamado email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getSolicitante() {
        return solicitante;
    }

    public Chamado solicitante(User user) {
        this.solicitante = user;
        return this;
    }

    public void setSolicitante(User user) {
        this.solicitante = user;
    }

    public User getExecutante() {
        return executante;
    }

    public Chamado executante(User user) {
        this.executante = user;
        return this;
    }

    public void setExecutante(User user) {
        this.executante = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chamado chamado = (Chamado) o;
        if(chamado.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, chamado.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Chamado{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", empresa='" + empresa + "'" +
            ", problema='" + problema + "'" +
            ", descricao='" + descricao + "'" +
            ", severidade='" + severidade + "'" +
            ", sugestao='" + sugestao + "'" +
            ", email='" + email + "'" +
            '}';
    }
}
