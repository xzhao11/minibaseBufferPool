TEX=latex
DVIPS=dvips
HTML=latex2html
HTMLDIR=html

all:SM_Join.ps

.SUFFIXES: .dvi .tex .ps

.tex.dvi: 
	$(TEX) $*.tex
	$(TEX) $*.tex
	$(HTML) -dir $(HTMLDIR) *.tex

.dvi.ps: 
	$(DVIPS) -f $*.dvi > $*.ps

SM_Join.ps: SM_Join.dvi

clean:
	/bin/rm -f *.log *.dvi *.aux
