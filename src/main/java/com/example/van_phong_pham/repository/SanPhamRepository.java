package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.DanhMuc;
import com.example.van_phong_pham.model.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    // Search by name (contains, case-insensitive)
    @Query("SELECT sp FROM SanPham sp WHERE sp.ten_sanpham LIKE CONCAT('%', :keyword, '%')")
    List<SanPham> findByTen_sanphamContainingIgnoreCase(@Param("keyword") String keyword);

    // Filter by category
    List<SanPham> findByDanhMuc(DanhMuc danhMuc);
    
    @Query("SELECT sp FROM SanPham sp WHERE sp.danhMuc.id_danhmuc = :danhMucId")
    List<SanPham> findByDanhMucId_danhmuc(@Param("danhMucId") Integer danhMucId);

    // Filter by price range
    List<SanPham> findByGiaBetween(Double minPrice, Double maxPrice);

    // Filter by brand
    @Query("SELECT sp FROM SanPham sp WHERE sp.thuong_hieu LIKE CONCAT('%', :brand, '%')")
    List<SanPham> findByThuong_hieuContainingIgnoreCase(@Param("brand") String brand);

    // Products in stock
    @Query("SELECT sp FROM SanPham sp WHERE sp.so_luong > :quantity")
    List<SanPham> findBySo_luongGreaterThan(@Param("quantity") Integer quantity);

    // Products low stock (for admin warning)
    @Query("SELECT sp FROM SanPham sp WHERE sp.so_luong < :quantity")
    List<SanPham> findBySo_luongLessThan(@Param("quantity") Integer quantity);

    // Active products only
    @Query("SELECT sp FROM SanPham sp WHERE sp.trang_thai = true")
    List<SanPham> findByTrang_thaiTrue();

    // Override findAll to use EntityGraph and prevent N+1 queries
    @EntityGraph(attributePaths = {"danhMuc"})
    @Override
    List<SanPham> findAll();

    // Products on sale
    @EntityGraph(attributePaths = {"danhMuc"})
    @Query("SELECT sp FROM SanPham sp WHERE sp.gia_giam IS NOT NULL")
    List<SanPham> findByGia_giamIsNotNull();

    // Multi-filter search with JPQL
    @EntityGraph(attributePaths = {"danhMuc"})
    @Query("SELECT sp FROM SanPham sp WHERE " +
           "(:keyword IS NULL OR sp.ten_sanpham LIKE CONCAT('%', :keyword, '%')) AND " +
           "(:danhMucId IS NULL OR sp.danhMuc.id_danhmuc = :danhMucId) AND " +
           "(:minPrice IS NULL OR sp.gia >= :minPrice) AND " +
           "(:maxPrice IS NULL OR sp.gia <= :maxPrice) AND " +
           "(:thuongHieu IS NULL OR sp.thuong_hieu LIKE CONCAT('%', :thuongHieu, '%')) AND " +
           "(sp.trang_thai = true)")
    Page<SanPham> findByMultiFilter(
            @Param("keyword") String keyword,
            @Param("danhMucId") Integer danhMucId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("thuongHieu") String thuongHieu,
            Pageable pageable);

    // Count by category
    long countByDanhMuc(DanhMuc danhMuc);

    // Count active products
    @Query("SELECT COUNT(sp) FROM SanPham sp WHERE sp.trang_thai = true")
    long countByTrang_thaiTrue();

    // Pageable findAll for active products
    @Query("SELECT sp FROM SanPham sp WHERE sp.trang_thai = true")
    Page<SanPham> findByTrang_thaiTrue(Pageable pageable);
}
