function svd_IR(filename)
%pwd
functionpath = strcat('../data/',filename);
X = csvread(functionpath);
[U,S,V] = svd(X);
%csvwrite(strcat('../data/U_',filename),U);
csvwrite(strcat('../data/S_',filename),S);
V = V';
csvwrite(strcat('../data/V_',filename),V(1:5,:));
end
