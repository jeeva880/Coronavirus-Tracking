package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.demo.model.LocationStates;
import com.example.demo.services.CoronaVirusDataServices;
import com.example.demo.services.CovidRepository;


@Controller
public class HomeController 
{
	
	CoronaVirusDataServices crnService;
	
	@Autowired
	public void setCrnService(CoronaVirusDataServices crnService) {
		this.crnService = crnService;
	}
	
	@Autowired
	private CovidRepository covidrepository;


	@GetMapping("/")
	public String home(Model model)
	{
		List<LocationStates> allstates=crnService.getAllstates();
		int totalDeathsReported=allstates.stream().mapToInt(stat->stat.getLatestTotalDeaths()).sum();
		model.addAttribute("LocationStates",allstates);
		model.addAttribute("totalDeathsReported",totalDeathsReported);
		covidrepository.saveAll(allstates);
		return "home";
	}
	
	@RequestMapping(path = "/collectionChartData",produces= {"application/json"})
	@ResponseBody
	public List<LocationStates> collectChartData(Model model){
		System.out.println("Here View Char Data..");
		List<LocationStates> allstates=crnService.getAllstates();
		int totalDeathsReported=allstates.stream().mapToInt(stat->stat.getLatestTotalDeaths()).sum();
		model.addAttribute("LocationStates",allstates);
		model.addAttribute("totalDeathsReported",totalDeathsReported);
		return allstates;
			
	}
	@RequestMapping(path = "/collectionChartData/{id}",produces= {"application/json"})
	@ResponseBody
	public Optional<LocationStates> collectChartDataByCountry(@PathVariable("id") int countryid,Model model){
		System.out.println("Here View Char Data by Country id..");
		Optional<LocationStates> locationStates=covidrepository.findById(countryid);
		return locationStates;
	}

	
	@RequestMapping(path = "/collectionChartData/country={name}",produces= {"application/json"})
	@ResponseBody
	public LocationStates collectChartDataByCountryName(@PathVariable("name") String countryName,Model model){
		System.out.println("Here View Char Data by Country name..");
		LocationStates locationStates=covidrepository.findByCountry(countryName);
		return locationStates;
	}
	
	@RequestMapping(path = "/collectionChartData/top={count}",produces= {"application/json"})
	@ResponseBody
	public List<LocationStates> collectChartDataByCountryCount(@PathVariable("count") int count,Model model){
		System.out.println("Here View Char Data by Country id..");
		List<LocationStates> locationStates=covidrepository.findcountryByLatestTotalDeaths(count);
		return  locationStates;
	}

	@RequestMapping(value ="/ViewChart",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView ViewChart() {
		return new ModelAndView("ViewChart").addObject("myURL", new String("http://localhost:8082/collectionChartData"));
	}
	
	@GetMapping(path="/ViewChart/{id}",produces={"application/json"})
	public String ViewCountryById(@PathVariable("id") int id,Model m) {
		m.addAttribute("id",id);
		m.addAttribute("http://localhost:8082/collectionChartData/"+id);
		return "ViewChart";
	}
	
}
