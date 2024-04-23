public interface HeaderCategoryRepository extends JpaRepository<HeaderCategory,Integer> {
    Page<HeaderCategory> findAll(Pageable pageable);
}
