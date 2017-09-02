package com.moditraders.services;

import java.util.Collection;

import com.moditraders.models.AuthInfo;
import com.moditraders.models.StateModel;
import com.moditraders.models.UserModel;

public interface IUserService {
	
	public boolean login(AuthInfo authInfo);

	public UserModel getUserInfo();

	public Collection<StateModel> getStates();
	
}
