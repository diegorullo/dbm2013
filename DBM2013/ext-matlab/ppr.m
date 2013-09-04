function p = getPersonalizedPageRank(L,pos,d)
 % return PageRank vector
 %
 % input:
 % L = Link Matrix (=rotated adjacency matrix)
 % d = constant parameter
 %
 % Example from Kardi Teknomo's Page Rank tutorial
 % is given as input and output of this function.
 % Read the full tutorial for more explanation.
 %
 % (c) 2012 Kardi Teknomo % http://people.revoledu.com/kardi/tutorial/

if nargin<3
	d = 0.85;
end

[m n] = size(L);
c = sum(L); %somma valori colonne della matrice
L_c = L./repmat(c, m, 1);
k=0;

while 1
	k = k+1;
	for i=1:m
		if(i==pos)
			p(i) = (1-d)+d*(L_c(i,:)*c');
		else
			p(i) = d*(L_c(i,:)*c');
		end
	end
	c=p;
	if sum(sum(p))==m || k>256
		break;
	end
end

p = p./sum(p);	%per normalizzare
p = p';