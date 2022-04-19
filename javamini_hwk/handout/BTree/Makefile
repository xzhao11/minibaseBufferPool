
TEX=latex
DVIPS=dvips
HTML=latex2html
HTMLDIR=html

all:BTree.ps

.SUFFIXES: .dvi .tex .ps

.tex.dvi: 
	$(TEX) $*.tex
	$(TEX) $*.tex
	$(HTML) -dir $(HTMLDIR) *.tex

.dvi.ps: 
	$(DVIPS) -f $*.dvi > $*.ps

BTree.ps: BTree.dvi

clean:
	/bin/rm -f *.log *.dvi *.aux
