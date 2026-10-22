package com.rps.finwallet.xlearning;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ============================================================================
 * 🤝 @ManyToMany Example: Project Entity
 * ============================================================================
 * Inverse side of the @ManyToMany relationship (indicated by 'mappedBy').
 * Is table me koi extra foreign key column nahi hoti,
 * kyunki connection 'developer_projects' junction table me maintain hota hai.
 */
@Entity
@Table(name = "learning_projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 50)
    private String clientName;

    // Inverse side of @ManyToMany: mappedBy points to the 'projects' field in Developer entity
    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    private Set<Developer> developers = new HashSet<>();

    public Project() {
    }

    public Project(String title, String clientName) {
        this.title = title;
        this.clientName = clientName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public Set<Developer> getDevelopers() { return developers; }
    public void setDevelopers(Set<Developer> developers) { this.developers = developers; }

    // Best Practice for JPA Entities in Sets: equals & hashCode based on natural ID or ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project project)) return false;
        return id != null && id.equals(project.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
