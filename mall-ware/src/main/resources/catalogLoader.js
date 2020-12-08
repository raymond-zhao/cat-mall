$(function () {
    $.getJSON("index/catalog.json", function (data) {
        const ctgall = data;
        $(".header_main_left_a").each(function () {
            const ctgnums = $(this).attr("ctg-data");
            if (ctgnums) {
                const panel = $("<div class='header_main_left_main'></div>");
                const panelol = $("<ol class='header_ol'></ol>");
                const ctgnumArray = ctgnums.split(",");
                $.each(ctgnumArray, function (i, ctg1Id) {
                    const ctg2list = ctgall[ctg1Id];
                    $.each(ctg2list, function (i, ctg2) {
                        const cata2link = $("<a href='#' style= 'color: #111;' class='aaa'>" + ctg2.name + "  ></a>");
                        const li = $("<li></li>");
                        const ctg3List = ctg2["catalog3List"];
                        let len = 0;
                        $.each(ctg3List, function (i, ctg3) {
                            const cata3link = $("<a href=\"http://search.catmall.com/list.html?catalog3Id=" + ctg3.id + "\" style=\"color: #999;\">" + ctg3.name + "</a>");
                            li.append(cata3link);
                            len = len + 1 + ctg3.name.length;
                        });
                        if (len >= 46 && len < 92) {
                            li.attr("style", "height: 60px;");
                        } else if (len >= 92) {
                            li.attr("style", "height: 90px;");
                        }
                        panelol.append(cata2link).append(li);
                    });
                });
                panel.append(panelol);
                $(this).after(panel);
                $(this).parent().addClass("header_li2");
            }
        });
    });
});