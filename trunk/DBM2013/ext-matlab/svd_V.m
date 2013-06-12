function svd_V(filename)
	%pwd
	functionpath = strcat('../data/',filename);
	X = csvread(functionpath);
	[U,S,V] = svd(X);
	%csvwrite(strcat('../data/U_',filename),U);
	csvwrite(strcat('../data/S_',filename),S);
	V = V';
	%csvwrite(strcat('../data/V_',filename),V);
	
	% limitiamo la scrittura di V' al massimo numero di righe presenti
	% nel caso questo sia minore di 5.
	[num_r, num_c] = size(V);
	limit = 5;
	if(num_r < 5)
		limit = num_r;
	end
	csvwrite(strcat('../data/V_',filename),V(1:limit,:));
end
