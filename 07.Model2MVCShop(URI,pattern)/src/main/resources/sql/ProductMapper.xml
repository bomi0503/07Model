<?xml version="1.0" encoding="UTF-8" ?>

<!DOCTYPE mapper
		PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
		"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="ProductMapper">
 	
	<resultMap id="productSelectMap" type="product">
		<result property="prodNo" 			column="prod_no" 			jdbcType="INTEGER"/>
		<result property="prodName"			column="prod_name" 			jdbcType="VARCHAR" />
		<result property="prodDetail" 		column="prod_detail" 		jdbcType="VARCHAR" />
		<result property="manuDate" 		column="manufacture_day" 	jdbcType="VARCHAR" />
		<result property="price" 			column="price" 				jdbcType="NUMERIC" />
		<result property="fileName" 		column="image_file" 		jdbcType="VARCHAR" />
		<result property="regDate" 			column="reg_date" 			jdbcType="DATE" />
		<result property="proTranCode" 		column="tran_status_code" 	jdbcType="VARCHAR" />
	</resultMap>
	
	<!-- SQL : INSERT -->
	<insert 	id="addProduct"		parameterType="product" >
	 	INSERT
		INTO product 
		VALUES	 (	seq_product_prod_no.nextval ,
					#{prodName} ,
					#{prodDetail:VARCHAR} ,
					#{manuDate:VARCHAR} ,
					#{price:NUMERIC} ,
					#{fileName:VARCHAR} ,
					SYSDATE )
	 </insert>
	 
	 <!-- SQL : SELECT ONE -->
	 <select 	id="getProduct"	parameterType="string"	resultMap="productSelectMap">
		SELECT
		prod_no, prod_name, prod_detail, manufacture_day,price, image_file, reg_date
		FROM product 
		WHERE prod_no = #{value}
	 </select>
	 
	 <!-- SQL : UPDATE -->
	 <update	id="updateProduct"	parameterType="product" >
	   	UPDATE product
	   	<set>
	   		prod_name 		= #{prodName} ,
			prod_detail		= #{prodDetail},
			manufacture_day = #{manuDate} ,
			price			= #{price},
			image_file		= #{fileName}	
	   	</set>
	   	WHERE prod_no = #{prodNo}
	 </update>
		 
	<!-- SQL : SELECT LIST -->
	<select  id="getProductList"  parameterType="search"	resultMap="productSelectMap">
	  	SELECT *
	  	FROM (	SELECT inner_table.* , ROWNUM AS row_seq
	  					FROM		(	SELECT p.prod_no, p.prod_name, p.prod_detail, p.manufacture_day,
	  										   p.price, p.image_file, p.reg_date, t.tran_status_code
	  										FROM product p, transaction t
	  										WHERE p.prod_no = t.prod_no(+)
											<if test="searchCondition != null">
													<if test="searchCondition == 0 and searchKeyword !='' ">
										 				AND p.prod_no LIKE '%'||#{searchKeyword}||'%'
													</if>
													<if test="searchCondition == 1 and searchKeyword !='' ">
											 			AND p.prod_name LIKE '%'||#{searchKeyword}||'%'
													</if>
													<if test="searchCondition == 2 and searchKeyword !='' ">
										 				AND p.price LIKE '%'||#{searchKeyword}||'%'
													</if>
											</if>
											<choose>
												<when test="searchSortingOption != null">
													<if test="searchSortingOption == 0">
											 			ORDER BY p.price
													</if>
													<if test="searchSortingOption == 1">
											 			ORDER BY p.price DESC
													</if>
												</when>
												<otherwise>
													ORDER BY p.prod_no
												</otherwise>
											</choose>
											
						 ) inner_table
						WHERE ROWNUM &lt;= #{endRowNum} )
						WHERE row_seq BETWEEN #{startRowNum} AND #{endRowNum} 
	 </select>
	
	<!-- SQL : SELECT ROW Count -->	 
	 <select  id="getTotalCount"  parameterType="search"	 resultType="int">
	  	SELECT COUNT(*)
	  	FROM(	SELECT prod_no, prod_name, prod_detail, manufacture_day,price, image_file, reg_date
						FROM product
						<if test="searchCondition != null">
							<where>
								<if test="searchCondition == 0 and searchKeyword !='' ">
						 			prod_no LIKE '%'||#{searchKeyword}||'%'
								</if>
								<if test="searchCondition == 1 and searchKeyword !='' ">
						 			prod_name LIKE '%'||#{searchKeyword}||'%'
								</if>
								<if test="searchCondition == 2 and searchKeyword !='' ">
						 			price LIKE '%'||#{searchKeyword}||'%'
								</if>
							</where>
						</if>
						<choose>
							<when test="searchSortingOption != null">
								<if test="searchSortingOption == 0">
									ORDER BY price
								</if>
								<if test="searchSortingOption == 1">
									ORDER BY price DESC
								</if>
							</when>
							<otherwise>
								ORDER BY prod_no
							</otherwise>
						</choose>
								 ) countTable						
	 </select>
	 
</mapper>