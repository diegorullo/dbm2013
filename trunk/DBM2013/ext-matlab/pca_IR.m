function pca_IR(filename)
path = strcat('../../data/',filename)
X = csvread(path);
[pc,score,latent] = princomp(X);
csvwrite(strcat('../../data/pc_',filename),pc);
csvwrite(strcat('../../data/score_',filename),score);
csvwrite(strcat('../../data/latent_',filename),latent);
end