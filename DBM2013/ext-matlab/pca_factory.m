function pca_factory(filename)
%pwd
functionpath = strcat('../data/',filename);
A = csvread(functionpath);

[n m] = size(A);
AMean = mean(A);
AStd = std(A);
B = (A - repmat(AMean,[n 1])) ./ repmat(AStd,[n 1]);

[V D] = eig(cov(B));

cumsum(flipud(diag(D))) / sum(diag(D));

PC = B * V;

csvwrite(strcat('../data/FactoryCovB_',filename),cov(B));
csvwrite(strcat('../data/FactoryPC_',filename),PC);
csvwrite(strcat('../data/FactoryB_',filename),B);
csvwrite(strcat('../data/FactoryV',filename),V);

[COEFF,latent] = pcacov(cov(B));

csvwrite(strcat('../data/pcacovCOEFF_',filename),COEFF);
csvwrite(strcat('../data/pcacovLatent',filename),latent);

[pc,score,latent] = princomp(A);
csvwrite(strcat('../data/A_pc_',filename),pc);
csvwrite(strcat('../data/A_score_',filename),score);
csvwrite(strcat('../data/A_latent_',filename),latent);

[Bpc,Bscore,Blatent] = princomp(B);
csvwrite(strcat('../data/B_pc_',filename),pc);
csvwrite(strcat('../data/B_score_',filename),score);
csvwrite(strcat('../data/B_latent_',filename),latent);

end