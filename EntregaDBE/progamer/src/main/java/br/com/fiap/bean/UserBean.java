package br.com.fiap.bean;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.model.file.UploadedFile;

import br.com.fiap.dao.UserDao;
import br.com.fiap.model.Setup;
import br.com.fiap.model.User;

@Named
@RequestScoped
public class UserBean {

	private User user = new User();
	
	private UserDao userDao = new UserDao();
	
	private UploadedFile image;
	
	public void save()  {

		userDao.create(getUser());
		
		FacesContext
			.getCurrentInstance()
			.addMessage(null, new FacesMessage("Usuário cadastrado com sucesso"));
		
	}
	
	public String save1() throws IOException {
		System.out.println(this.user);
		
		System.out.println(image.getFileName()); 
		
		ServletContext servletContext = (ServletContext) FacesContext
															.getCurrentInstance()
															.getExternalContext()
															.getContext();
		String servletPath = servletContext.getRealPath("/");
		
		System.err.println(servletPath);
		
		FileOutputStream out = 
				new FileOutputStream(servletPath + "\\images\\" + image.getFileName());
		out.write(image.getContent());
		out.close();
		
		user.setImagePath("\\images\\" + image.getFileName());
		
		userDao.create(user);
		
		FacesContext
			.getCurrentInstance()
			.addMessage(null, new FacesMessage("Setup cadastrado com sucesso"));
		
		return "setups";
	}
	
	public List<User> getList() {
		return userDao.listAll();
	}
	
	public String login(){
		if (userDao.exist(user)) {
			//salvar o usuario logado na secao
			FacesContext
				.getCurrentInstance()
				.getExternalContext()
				.getSessionMap()
				.put("user", user);
			
			return "setups";
		}
		
		FacesContext
			.getCurrentInstance()
			.getExternalContext()
			.getFlash()
			.setKeepMessages(true);
		
		FacesContext
			.getCurrentInstance()
			.addMessage(null, new FacesMessage("Login inválido"));

		return "login?faces-redirect=true";
	}
	
	public String logout() {
		FacesContext
			.getCurrentInstance()
			.getExternalContext()
			.getSessionMap()
			.remove("user");
		
		return "login";
	}
	
	public String delete(User user) {
		userDao.remove(user);
		
		FacesContext
			.getCurrentInstance()
			.getExternalContext() 
			.getFlash()
			.setKeepMessages(true);
		
		FacesContext
			.getCurrentInstance()
			.addMessage(null, new FacesMessage("Setup apagado com sucesso"));
		
		return "setups?faces-redirect=true";

	}
	
	public void edit() {
		userDao.update(user);
		
		FacesContext
			.getCurrentInstance()
			.addMessage(null, new FacesMessage("Visitante atualizado com sucesso"));
	}

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



}
