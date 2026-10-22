package com.rps.finwallet.xlearning;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ============================================================================
 * 🤝 @ManyToMany Example: Developer Entity (Owning Side)
 * ============================================================================
 * The Owning side defines the @JoinTable that creates the physical junction table
 * in the database (e.g. 'learning_developer_projects').
 */
@Entity
@Table(name = "learning_developers")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * OWNING SIDE OF @ManyToMany:
     * 1. @JoinTable creates the 3rd table in MySQL: 'learning_developer_projects'
     * 2. joinColumns: foreign key pointing to Developer (this entity)
     * 3. inverseJoinColumns: foreign key pointing to Project (target entity)
     * 4. Best Practice: Hamesha Set<T> use karein, List<T> nahi!
     *    (List use karne se Hibernate MultipleBagFetchException fekta hai aur Cartesian product banata hai).
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "learning_developer_projects",
            joinColumns = @JoinColumn(name = "developer_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    public Developer() {
    }

    public Developer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // =========================================================================
    // 🌟 HELPER SYNCHRONIZATION METHODS (CRITICAL FOR BIDIRECTIONAL MAPPINGS)
    // =========================================================================
    // Hinglish: Dono taraf (Developer aur Project) me relationship ko memory me
    // sync rakhne ke liye ye helper methods hamesha likhne chahiye!
    public void addProject(Project project) {
        this.projects.add(project);
        project.getDevelopers().add(this);
    }

    public void removeProject(Project project) {
        this.projects.remove(project);
        project.getDevelopers().remove(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Project> getProjects() { return projects; }
    public void setProjects(Set<Project> projects) { this.projects = projects; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Developer that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
