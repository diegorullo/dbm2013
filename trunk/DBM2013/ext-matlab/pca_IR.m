function pca_IR(filename)
%pwd
functionpath = strcat('../data/',filename);
X = csvread(functionpath);
[COEFF,SCORE,latent] = princomp(X);
csvwrite(strcat('../data/PCA_',filename),COEFF(1:5,:));
%csvwrite(strcat('../data/SCORE_',filename),SCORE);
csvwrite(strcat('../data/latent_',filename),latent);
end