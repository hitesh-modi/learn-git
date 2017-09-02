package com.moditraders.services;

import com.moditraders.models.AuthInfo;
import com.moditraders.models.UserModel;

public interface IUserService {
	
	public boolean login(AuthInfo authInfo);

	public UserModel getUserInfo();
	
}
