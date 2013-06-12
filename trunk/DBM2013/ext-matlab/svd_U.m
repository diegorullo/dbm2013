function svd_U(filename)
	%pwd
	functionpath = strcat('../data/',filename);
	X = csvread(functionpath);
	[U,S,V] = svd(X);
	csvwrite(strcat('../data/U_',filename),U);
	%csvwrite(strcat('../data/S_',filename),S);
	%V = V';
	%csvwrite(strcat('../data/V_',filename),V);
	
	% limitiamo la scrittura di U al massimo numero di righe presenti
	% nel caso questo sia minore di 3.
	[num_r, num_c] = size(U);
	limit = 3;
	if(num_r < 3)
		limit = num_r;
	end
	csvwrite(strcat('../data/U_',filename),U(1:limit,:));
end
