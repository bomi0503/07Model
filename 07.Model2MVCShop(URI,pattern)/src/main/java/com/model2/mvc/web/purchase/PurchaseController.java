package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;


//==> 회원관리 Controller
@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping(value="addPurchaseView", method = RequestMethod.GET)
	public ModelAndView addPurchaseView(@RequestParam("prod_no") int prodNo, HttpSession session) throws Exception {

		System.out.println("/purchase/addPurchaseView");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user",session.getAttribute("user"));
		modelAndView.addObject("product", productService.getProduct(prodNo));
		modelAndView.setViewName("/purchase/addPurchaseView.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="addPurchase", method = RequestMethod.POST)
	public ModelAndView addPurchase( @ModelAttribute("purchase") Purchase purchase,HttpSession session, @RequestParam("prodNo")int prodNo  ) throws Exception {

		System.out.println("/purchase/addPurchase");
		
		purchase.setBuyer((User)session.getAttribute("user")); 
		purchase.setPurchaseProd(productService.getProduct(prodNo)); 
		purchaseService.addPurchase(purchase); 
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase",purchase);
		modelAndView.setViewName("forward:/purchase/addPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="getPurchase", method = RequestMethod.GET)
	public ModelAndView getPurchase( @RequestParam("tranNo") int tranNo) throws Exception {
		
		System.out.println("/purchase/getPurchase");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase",purchaseService.getPurchase(tranNo));
		modelAndView.setViewName("/purchase/getPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchaseView", method = RequestMethod.GET)
	public ModelAndView updatePurchaseView( @RequestParam("tranNo") int tranNo) throws Exception{

		System.out.println("/purchase/updatePurchaseView");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase",purchaseService.getPurchase(tranNo));
		modelAndView.setViewName("/purchase/updatePurchaseView.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchase", method = RequestMethod.POST)
	public ModelAndView updatePurchase( @RequestParam("tranNo")int tranNo, @ModelAttribute("purchase")Purchase purchase ) throws Exception{

		System.out.println("/purchase/updatePurchase");
		
		purchaseService.updatePurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/getPurchase?tranNo="+tranNo);
		
		return modelAndView;
	}
	
	//@RequestMapping("/updateTranCodeByProd.do")
	@RequestMapping(value="updateTranCodeByProd", method = RequestMethod.GET)
	public ModelAndView updateTranCodeByProd( @ModelAttribute("product") Product product  ) throws Exception{

		System.out.println("/purchase/updateTranCodeByProd");
		
		Purchase purchase = new Purchase();
		purchase.setPurchaseProd(product);
		System.out.println("왜왜왜오 : " + purchase);
		purchase.setTranCode("2");
		purchaseService.updateTranCode(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/product/listProduct?menu=manage");
		
		return modelAndView;
	}
	
	//@RequestMapping("/updateTranCode.do")
	@RequestMapping(value="updateTranCode", method = RequestMethod.GET)
	public ModelAndView updateTranCode(@ModelAttribute("purchase") Purchase purchase ) throws Exception{

		System.out.println("/purchase/updateTranCode");
		
		purchase.setTranCode("3");
		purchaseService.updateTranCode(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/listPurchase");
		
		return modelAndView;
	}	
	
	@RequestMapping( value="listPurchase" )
	public ModelAndView listPurchase( @ModelAttribute("search") Search search ,HttpSession session) throws Exception{
		
		System.out.println("/purchase/listPurchase");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		User user = (User)session.getAttribute("user"); 
		Map<String , Object> map=purchaseService.getPurchaseList(search,user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("list",map.get("list")); 
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		
		return modelAndView;
	}
}