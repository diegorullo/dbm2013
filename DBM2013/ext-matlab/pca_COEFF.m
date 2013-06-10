function pca_IR(filename)
	%pwd
	functionpath = strcat('../data/',filename);
	X = csvread(functionpath);
	[COEFF,SCORE,latent] = princomp(X);
	
	% limitiamo la scrittura di COEFF e latent
	% al massimo numero di righe presenti
	% nel caso questo sia minore di 5.
	[num_r, num_c] = size(COEFF);
	limit = 5;
	if(num_r < 5)
		limit = num_r;
	end
	
	csvwrite(strcat('../data/PCA_',filename),COEFF(1:limit,:));
	%csvwrite(strcat('../data/SCORE_',filename),SCORE);	
	csvwrite(strcat('../data/latent_',filename),latent(1:limit,:));
end