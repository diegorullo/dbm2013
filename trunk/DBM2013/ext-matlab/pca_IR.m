function pca_IR(filename)
%pwd
functionpath = strcat('../data/',filename);
X = csvread(functionpath);
[pc,score,latent] = princomp(X, 'econ');
csvwrite(strcat('../data/pc_',filename),pc);
csvwrite(strcat('../data/score_',filename),score);
csvwrite(strcat('../data/latent_',filename),latent);
end